package org.corefine.common.web.mvc;

import org.corefine.common.web.constant.CodeConstant;
import org.corefine.common.web.vo.DataVo;
import org.corefine.common.web.vo.MessageVo;
import org.corefine.common.web.vo.StatusVo;
import org.corefine.common.web.vo.TotalVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * mvc异常工具
 *
 * @author Fe 2016年5月16日
 */
public class BasicController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final StatusVo SUCCESS = new StatusVo();
    protected static final StatusVo ERROR = new StatusVo(CodeConstant.ERROR);

    protected MessageVo error(String message) {
        return new MessageVo(CodeConstant.ERROR, message);
    }

    protected MessageVo success(String message) {
        return new MessageVo(message);
    }

    protected <T> TotalVo<T> total(List<T> data, Long total) {
        return new TotalVo<>(data, total);
    }

    protected <T> DataVo<T> data(T data) {
        return new DataVo<>(data);
    }
}
