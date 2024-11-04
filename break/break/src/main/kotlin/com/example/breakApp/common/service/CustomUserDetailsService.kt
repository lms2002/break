package com.example.breakApp.common.service

import com.example.breakApp.common.dto.CustomUser
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        memberRepository.findByLoginId(username)
            ?.let { createUserDetails(it) } ?: throw UsernameNotFoundException("해당 유저는 없습니다.")

    private fun createUserDetails(member: Member): UserDetails =
        CustomUser(
            member.userId!!,
            member.loginId,
            member.password,  // 해시된 비밀번호를 그대로 사용
            member.memberRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") }
        )
}