package com.marcofaccani.awssqs.config.props;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class AppProperties {

  private final AwsConfigProperties awsConfigProperties;

}
