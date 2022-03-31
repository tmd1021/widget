package edu.sru.cpsc.webshopping.repository.stats;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;


import edu.sru.cpsc.webshopping.domain.user.Statistics;
import edu.sru.cpsc.webshopping.domain.user.Statistics.Category;


public interface StatsRepository extends CrudRepository<Statistics, Long> {
	Statistics[] findByDate(LocalDateTime date);
	Statistics[] findByHour(int hour);
	Statistics[] findByCategory(Category category);
}
