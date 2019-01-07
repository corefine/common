package org.corefine.common.feign;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientRegistrar.class)
public class FeignAutoConfiguration {
}
