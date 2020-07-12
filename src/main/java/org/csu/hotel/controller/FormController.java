package org.csu.hotel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.hotel.domain.Finance;
import org.csu.hotel.domain.Room;
import org.csu.hotel.domain.Stay;
import org.csu.hotel.service.FinanceService;
import org.csu.hotel.service.RoomService;
import org.csu.hotel.service.StayService;
import org.csu.hotel.util.LayerData;
import org.csu.hotel.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("form/")
public class FormController {

    @Autowired
    private FinanceService financeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private StayService stayService;

    @PostMapping("finance/day")
    public LayerData<Finance> getFormsByday(@RequestParam(value="page",defaultValue = "1")Integer page,
                                            @RequestParam(value="limit",defaultValue = "10")Integer limit,
                                            @RequestParam(required = false) String date){

        if(date == null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            date =  df.format(new Date());
        }

        QueryWrapper<Finance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",date);

        LayerData<Finance> layerData = new LayerData<>();

        Page<Finance> financialPage = (Page<Finance>) financeService.page(new Page<>(page,limit),queryWrapper);

        System.out.println(financialPage.getTotal());
        layerData.setCount((int) financialPage.getTotal());

        layerData.setCode(200);
        layerData.setMsg("获取成功");
        layerData.setData(financialPage.getRecords());

        return layerData;

    }


    @PostMapping("finance/month")
    public RestResponse getFormsByMonth(@RequestParam(value="page",defaultValue = "1")Integer page,
                                        @RequestParam(value="limit",defaultValue = "10")Integer limit,
                                        @RequestParam(required = false) String date){

        if(date == null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
            date =  df.format(new Date());
        }

        QueryWrapper<Finance> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("date",date);
        System.out.println(date);

        RestResponse restResponse = RestResponse.success("成功");


        List<Finance> financeList = financeService.list(queryWrapper);

        List<Finance> returnList = new ArrayList<>();

        //统计该月份每一天的财务情况
        int flag = 0;
        for(Finance f: financeList){
            for(Finance rf:returnList){

                if(rf.getDate().equals(f.getDate())){
                    flag = 1;
                    rf.setRoomPrice(rf.getRoomPrice() + f.getRoomPrice());
                    rf.setReplacePrice(rf.getReplacePrice() + f.getReplacePrice());
                    rf.setCommodityPrice(rf.getCommodityPrice() + f.getCommodityPrice());
                    rf.setTotalPrice(rf.getTotalPrice() + f.getTotalPrice());
                }

            }

            if(flag == 0){
                returnList.add(f);
            }

        }

        restResponse.setData(returnList);

        return restResponse;

    }

    @GetMapping("room")
    public RestResponse getAllRooms(){

        List<Room> roomList = roomService.getAllRooms();
        List<Stay> stayList = stayService.getAllStays();

        List<Map> mapList = new ArrayList<>();

        for(Room r:roomList){
            Map<String,Object> map = new HashMap<>();

            map.put("roomId",r.getRoomId());
            map.put("floor",r.getFloor());
            map.put("roomType",r.getRoomType().getName());
            map.put("price",r.getRoomType().getPrice());
            map.put("status",r.getStatus());

            if(r.getStatus().equals("N")){
                Stay stay = stayService.getStayByRoomId(r.getRoomId());
                map.put("userName",stay.getTenant().getName());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");//设置日期格式
                String startDate =  df.format(stay.getStayStartTime());
                map.put("stayStartTime",startDate);

                String endDate = df.format(stay.getStayEndTime());
                map.put("stayEndTime",endDate);
            }

            mapList.add(map);
        }


        RestResponse restResponse = RestResponse.success("成了");
        restResponse.setData(mapList);
        return restResponse;

    }





}