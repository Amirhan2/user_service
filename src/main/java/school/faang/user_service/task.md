������� ������
����������� ����������� ������������ ������� ���� � ����������� �� �������, ��������� ������ �������������. ����� ���������, ��� ������������ ��� ��������������� �� ��� �������.

1. � ������ controller.event c������� ����� EventParticipationController, ���� ��� ���. �� ����� �������� �� ��������� �������� ������������ � ��������� ���� ��������. �������� ���, ����� �� ��� Spring bean. �������� � ���� ��� ������ EventParticipationService - ��� �������� � ��������� �������.

   * �������� ����� unregisterParticipant. ���� ����� ������ ���������� ���� ������������ � ������ � ����� unregisterParticipant ������ EventPartcipationService.

2. � ������ service.event �������� ����� EventParticipationService, ���� ��� ��� ���. �� ����� ��������� ������-������. �������� ��� Spring bean. �������� � ���� ��� ������ EventParticipationRepository. ���� ����� ��� ������������, ��� ������:

   * void register(long eventId, long userId);

   * void unregister(long eventId, long userId);

   * List<User> findAllParticipantsByEventId(long eventId);

   * int countParticipants(long eventId);

3. �������� � ������ EventParticipationService ����� unregisterParticipant(long eventId, long userId) ��� ������ ����������� ������������. ����� ���������, ��� ������ ������������ ��� ��������������� �� �������, � � ���� ������ �������� �����������. ����� ��������� ����������. ���������, ����� ������ EventParticipationRepository ����� ������������ ��� ���� ��������.

4. ������� ���� ���������� ��� unit-�������.