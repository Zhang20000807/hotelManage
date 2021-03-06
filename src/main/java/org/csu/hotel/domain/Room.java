package org.csu.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@TableName("room")
@ToString
@NoArgsConstructor
public class Room {
    private int roomId;
    private int floor;
    private String status;
    private int typeId;
    @TableField(exist =false)
    private RoomType roomType;

    public Room(int roomId, int floor, int typeId, String status) {
        this.roomId=roomId;
        this.floor=floor;
        this.typeId=typeId;
        this.status=status;
        this.setRoomType(new RoomType());
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
