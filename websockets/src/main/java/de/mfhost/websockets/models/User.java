package de.mfhost.websockets.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User {

    @Id
    String id;

    String name;

    Integer gender;
    List<String> tags;
    List<String> dislikes;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date birthday;
    List<String> looking_for;



}
