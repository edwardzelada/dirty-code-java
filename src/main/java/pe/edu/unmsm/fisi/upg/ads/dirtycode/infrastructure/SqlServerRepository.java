package pe.edu.unmsm.fisi.upg.ads.dirtycode.infrastructure;

import pe.edu.unmsm.fisi.upg.ads.dirtycode.domain.IRepository;
import pe.edu.unmsm.fisi.upg.ads.dirtycode.domain.Speaker;

public class SqlServerRepository implements IRepository {
	public int saveSpeaker(Speaker speaker) {
            return 1;
	}
}