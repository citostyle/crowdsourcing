package tuwien.aic.crowdsourcing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public HibernateAwareObjectMapper() {
		Hibernate4Module hm = new Hibernate4Module();
		registerModule(hm);
	}
}
