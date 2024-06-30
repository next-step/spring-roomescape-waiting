package roomescape.adapter.out.persistence;

import static roomescape.adapter.mapper.MemberMapper.*;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.adapter.mapper.MemberMapper;
import roomescape.adapter.out.persistence.repository.MemberRepository;
import roomescape.application.port.out.MemberPort;
import roomescape.domain.Member;

@Repository
public class MemberPersistenceAdapter implements MemberPort {

  private final MemberRepository memberRepository;

  public MemberPersistenceAdapter(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public List<Member> findMembers() {
    return memberRepository.findAll()
                           .stream()
                           .map(MemberMapper::mapToDomain)
                           .toList();
  }

  @Override
  public void saveMember(Member member) {
    memberRepository.save(mapToEntity(member));
  }

  @Override
  public Optional<Member> findMemberByEmail(String email) {
    return memberRepository.findByEmail(email)
                           .map(MemberMapper::mapToDomain);
  }
}
