package com.cos.navercrawapp.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
@Document(collection = "naver_realtime")
public class NaverNews {
   @Id
   private String _id;
   
   private String company;
   private String title;
   private Date createdAt;
}
