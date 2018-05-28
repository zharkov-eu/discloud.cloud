package ru.discloud.gateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.service.GroupService;
import ru.discloud.gateway.web.model.GroupResponse;
import ru.discloud.shared.web.core.GroupRequest;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/group")
public class GroupController {
  private final GroupService groupService;

  @Autowired
  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  @PostMapping()
  public Mono<GroupResponse> createGroup(@Valid @RequestBody GroupRequest groupRequest) throws ValidationException {
    return groupService.createGroup(groupRequest).map(GroupResponse::new);
  }

  @GetMapping(path = "/")
  public Mono<List<GroupResponse>> getGroups() {
    return groupService.getGroups().map(groups -> groups.stream().map(GroupResponse::new).collect(Collectors.toList()));
  }

  @GetMapping(path = "/{id}")
  public Mono<GroupResponse> getGroup(@PathVariable Integer id) {
    return groupService.getGroupById(id).map(GroupResponse::new);
  }

  @PatchMapping(path = "/{id}")
  public Mono<GroupResponse> updateGroup(@PathVariable Integer id, @Valid @RequestBody GroupRequest groupRequest) {
    return groupService.updateGroup(id, groupRequest).map(GroupResponse::new);
  }

  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteGroup(@PathVariable Integer id) {
    return groupService.deleteGroup(id);
  }
}
