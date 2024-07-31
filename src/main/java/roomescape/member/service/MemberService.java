package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.error.exception.MemberAlreadyExistsException;
import roomescape.error.exception.MemberNotExistsException;
import roomescape.error.exception.PasswordNotMatchedException;
import roomescape.login.LoginMember;
import roomescape.login.service.LoginMemberService;
import roomescape.member.Member;
import roomescape.member.MemberRole;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService implements LoginMemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findMembers() {
        return memberRepository.findAll().stream()
            .map(MemberResponse::new)
            .toList();
    }

    @Override
    public LoginMember getLoginMember(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotExistsException::new);

        if (!member.isMatchedPassword(password)) {
            throw new PasswordNotMatchedException();
        }

        return new LoginMember(member.getId(), member.getName(), member.getRole());
    }

    @Transactional
    public MemberResponse save(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
            throw new MemberAlreadyExistsException();
        }

        Member member = memberRepository.save(
            new Member(memberRequest.getEmail(), memberRequest.getPassword(),
                memberRequest.getName(), MemberRole.MEMBER));

        return new MemberResponse(member);
    }
}
