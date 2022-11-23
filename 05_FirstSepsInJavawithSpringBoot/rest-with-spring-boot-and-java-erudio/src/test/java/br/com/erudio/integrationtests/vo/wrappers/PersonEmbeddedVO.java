package br.com.erudio.integrationtests.vo.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.erudio.integrationtests.vo.PersonVO;

public class PersonEmbeddedVO implements Serializable{
	private static final long serialVersionUID = 1L;

	@JsonProperty("personVOList")
	private List<PersonVO> people;

	public PersonEmbeddedVO() {
	}

	public List<PersonVO> getPeople() {
		return people;
	}

	public void setPeople(List<PersonVO> people) {
		this.people = people;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((people == null) ? 0 : people.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonEmbeddedVO other = (PersonEmbeddedVO) obj;
		if (people == null) {
			if (other.people != null)
				return false;
		} else if (!people.equals(other.people))
			return false;
		return true;
	}
}
