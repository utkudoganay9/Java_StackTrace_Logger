package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.helper.ServerType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request", schema = "log")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "LogDate", nullable = false)
    private LocalDateTime logDate;

    @Column(name = "IpAddress", nullable = false)
    private String ipAddress;

    @Column(name = "UserId", nullable = false)
    private String userId;

    @Column(name = "Url", nullable = false)
    private String url;

    @Column(name = "ResponseCode", nullable = false)
    private Integer responseCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ServerType", nullable = false)
    private ServerType serverType;
}
