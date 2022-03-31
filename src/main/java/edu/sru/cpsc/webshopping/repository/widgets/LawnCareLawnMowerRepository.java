package edu.sru.cpsc.webshopping.repository.widgets;

import org.springframework.data.repository.CrudRepository;

import edu.sru.cpsc.webshopping.domain.widgets.LawnCare_LawnMower;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;

public interface LawnCareLawnMowerRepository<T extends LawnCare_LawnMower> extends CrudRepository<T, Long> {

}
