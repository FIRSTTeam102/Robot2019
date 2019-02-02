/*
 * The code for FRC Team #102's robot for the 2019 game, Destination: Deep Space
 * Copyright (C) 2019  Robotics Fund Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * Contact us at: firstteam102@gmail.com
 */

package org.team102.robots.robot2019.lib;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * A {@link PIDSource} created from a {@link Supplier} of {@code double}s
 */
public class SupplierPIDSource implements PIDSource {
	
	private Supplier<Double> supp;
	private PIDSourceType sourceType;
	
	/**
	 * Creates the source, with a {@link PIDSource source type} of {@link PIDSourceType#kDisplacement displacement}
	 * @param supp The {@code double} {@link Supplier}
	 */
	public SupplierPIDSource(Supplier<Double> supp) {
		this(supp, PIDSourceType.kDisplacement);
	}
	
	/**
	 * Creates the source
	 * @param supp The {@code double} {@link Supplier}
	 * @param type The {@link PIDSourceType type} of source this should be
	 */
	public SupplierPIDSource(Supplier<Double> supp, PIDSourceType type) {
		this.supp = supp;
		setPIDSourceType(type);
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return sourceType;
	}
	
	@Override
	public double pidGet() {
		return supp.get();
	}
}