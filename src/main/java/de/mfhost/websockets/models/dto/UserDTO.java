package de.mfhost.websockets.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private String id;
    String name;

    Integer gender;
    List<String> tags;
    List<String> dislikes;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date birthday;

}
