package com.evinceframework.web.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.evinceframework.data.Query;
import com.evinceframework.data.QueryParameters;
import com.evinceframework.data.impl.DefaultQueryParametersImpl;
import com.evinceframework.web.dojo.mvc.model.ViewModelUtils;
import com.evinceframework.web.stereotype.ServiceController;

/**
 * 
 * @author Craig Swing
 *
 */
@Controller
@ServiceController
public class QueryController implements BeanFactoryAware {

	private static final Log logger = LogFactory.getLog(QueryController.class);
	
	private static final String DEFAULT_VIEW = "data.query";
	
	private BeanFactory beanFactory;
	
	private ViewModelUtils modelUtil = new ViewModelUtils();
	
	@Override
	public void setBeanFactory(BeanFactory factory) throws BeansException {
		beanFactory = factory;
	}
	
	@RequestMapping(value="data/query/{qualifier}", method=RequestMethod.POST)
	public String query(String qualifier, ModelMap model) {
		
		String beanName = String.format("data.query.%s", qualifier);
		Query<?> queryCommand = null;
		
		if (beanFactory.containsBean(beanName)) {
			if (ClassUtils.isAssignable(beanFactory.getType(beanName), Query.class)) {
				logger.debug(String.format("Using the bean named %s to perform the query.", beanName));
				queryCommand = (Query<?>)beanFactory.getBean(beanName);
				
			} else {
				logger.warn(String.format(
						"A bean with the name %s exists but it doesn't implement the %s.", 
						beanName, Query.class.getCanonicalName()));
			}
		} else {
			logger.warn(String.format(
					"Unable to perform the query for %s. Implementation not found.", beanName));
		}
		
		if (queryCommand != null) {
			QueryParameters params = new DefaultQueryParametersImpl(); // TODO get defaults from post data?
			modelUtil.addToViewModel(model, queryCommand.execute(params));
		}
		
		return DEFAULT_VIEW;
	}	
}
