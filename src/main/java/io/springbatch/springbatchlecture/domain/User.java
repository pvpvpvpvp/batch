package io.springbatch.springbatchlecture.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String nickname;
    private String password;
    private String email;
    private String id;
    private Date insDate;
    private Date updDate;

//    @OneToMany(mappedBy = "userIdx")
//    private List<Orders> orders = new ArrayList<>();
//    @OneToMany(mappedBy = "userIdx")
//    private List<UserAsset> userAssets = new ArrayList<>();
}
