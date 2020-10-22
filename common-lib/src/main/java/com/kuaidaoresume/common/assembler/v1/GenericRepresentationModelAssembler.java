package com.kuaidaoresume.common.assembler.v1;

import com.kuaidaoresume.common.dto.PersistedEntityDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.GenericTypeResolver;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class GenericRepresentationModelAssembler<T extends PersistedEntityDto> implements SimpleRepresentationModelAssembler<T> {

	private final Class<?> controllerClass;

	@Getter
	private final LinkRelationProvider relProvider;

	@Getter
	private final Class<?> resourceType;

	@Getter
	@Setter
	private String basePath = "";

	public GenericRepresentationModelAssembler(Class<?> controllerClass, LinkRelationProvider relProvider) {
		this.controllerClass = controllerClass;
		this.relProvider = relProvider;
		this.resourceType = GenericTypeResolver.resolveTypeArgument(this.getClass(),
				GenericRepresentationModelAssembler.class);
	}

	public GenericRepresentationModelAssembler(Class<?> controllerClass) {
		this(controllerClass, new AnnotationLinkRelationProvider());
	}

	public void addLinks(EntityModel<T> resource) {
		resource.add(getCollectionLinkBuilder().slash(getId(resource)).withSelfRel());
		resource.add(getCollectionLinkBuilder().withRel(this.relProvider.getCollectionResourceRelFor(this.resourceType)));
	}

	private Object getId(EntityModel<T> resource) {
		T content = resource.getContent();
		return Objects.nonNull(content) ? content.getId() : "";
	}

	public void addLinks(CollectionModel<EntityModel<T>> resources) {
		resources.add(getCollectionLinkBuilder().withSelfRel());
	}

	public Link getSelfLink(EntityModel<T> resource) {
		return resource.getRequiredLink(IanaLinkRelations.SELF);
	}

	protected LinkBuilder getCollectionLinkBuilder() {
		WebMvcLinkBuilder linkBuilder = linkTo(this.controllerClass);
		for (String pathComponent : (getPrefix() + this.relProvider.getCollectionResourceRelFor(this.resourceType))
				.split("/")) {
			if (!pathComponent.isEmpty()) {
				linkBuilder = linkBuilder.slash(pathComponent);
			}
		}
		return linkBuilder;
	}

	private String getPrefix() {
		return getBasePath().isEmpty() ? "" : getBasePath() + "/";
	}
}
