package org.adbcj.perf;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;

/**
 *
 */
public class JdbcUpdateExperiment extends AbstractJDBCExperiment {
	private final int count;
	private String template;
	private final Random rand = new Random();

	public JdbcUpdateExperiment(Configuration configuration, String host, String template, int count) {
		super(configuration, host);
		this.count = count;
		this.template = template;
	}

	public void init() throws Exception {
		final Connection connection = connect();
		final Statement statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM updates");
		statement.executeUpdate("INSERT INTO updates (id) VALUES (1)");
	}

	public void execute() throws Exception {
		final Connection connection = connect();
		final Statement statement = connection.createStatement();
		for (int i = 0; i < count; i++) {
			final String sql = String.format(template, rand.nextInt());
			if (getConfiguration().isBatched()) {
				statement.addBatch(sql);
			} else {
				statement.executeUpdate(sql);
			}
		}
		if (getConfiguration().isBatched()) {
			statement.executeBatch();
		}
		statement.close();
	}
}
