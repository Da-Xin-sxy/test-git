public class BuildTreeService2 {

    public static void main(String[] args) {
        List<GroupDTO> groupDTOList = new ArrayList<>();
        GroupDTO groupDTO1 = new GroupDTO("1", "根节点", "0", "group", null, null);
        GroupDTO groupDTO2 = new GroupDTO("2", "节点2", "1", "group", null, null);
        GroupDTO groupDTO3 = new GroupDTO("3", "节点3", "1", "group", null, null);
        GroupDTO groupDTO4 = new GroupDTO("4", "节点4", "3", "group", null, null);
        groupDTOList.add(groupDTO1);
        groupDTOList.add(groupDTO2);
        groupDTOList.add(groupDTO3);
        groupDTOList.add(groupDTO4);

        List<ServerDTO> serverDTOList = new ArrayList<>();
        ServerDTO serverDTO1 = new ServerDTO("1", "192.168.1.1", "服务器1");
        ServerDTO serverDTO2 = new ServerDTO("2", "192.168.1.2", "服务器2");
        ServerDTO serverDTO3 = new ServerDTO("2", "192.168.1.3", "服务器3");
        ServerDTO serverDTO4 = new ServerDTO("3", "192.168.1.4", "服务器4");
        serverDTOList.add(serverDTO1);
        serverDTOList.add(serverDTO2);
        serverDTOList.add(serverDTO3);
        serverDTOList.add(serverDTO4);
        List<GroupDTO> groupDTOS = buildTree(groupDTOList, serverDTOList);
        System.out.println(groupDTOS);

    }


    public static List<GroupDTO> buildTree(List<GroupDTO> groupDTOList, List<ServerDTO> serverDTOList) {
        // 创建映射以通过 parentId 快速查找 GroupDTO
        Map<String, GroupDTO> groupMap = new HashMap<>();

        // 创建映射以通过 groupId 快速查找 ServerDTOs
        Map<String, List<ServerDTO>> serverMap = new HashMap<>();

        // 填充映射
        for (GroupDTO group : groupDTOList) {
            groupMap.put(group.getId(), group);
        }
        for (ServerDTO server : serverDTOList) {
            serverMap.computeIfAbsent(server.getId(), k -> new ArrayList<>()).add(server);
        }

        // 初始化根节点列表
        List<GroupDTO> rootGroups = new ArrayList<>();

        // 遍历映射，构建树结构
        for (GroupDTO group : groupDTOList) {
            String parentId = group.getParentId();
            if (Objects.equals(parentId, "0") || parentId.isEmpty()) {
                rootGroups.add(group);
            } else {
                GroupDTO parentGroup = groupMap.get(parentId);
                if (parentGroup != null) {
                    if (parentGroup.getGroups() == null) {
                        parentGroup.setGroups(new ArrayList<>());
                    }
                    parentGroup.getGroups().add(group);
                }
            }

            // 获取并添加服务器
            List<ServerDTO> serversForGroup = serverMap.get(group.getId());
            if (serversForGroup != null) {
                group.setServers(new ArrayList<>(serversForGroup));
            }
        }

        return rootGroups;
    }
}
