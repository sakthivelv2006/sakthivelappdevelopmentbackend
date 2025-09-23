package com.examly.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.*;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepository;

@RestController
@RequestMapping("/api/users/password")
public class PasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> resetTokens = new HashMap<>();

    @PostMapping("/reset/{email}")
    public ResponseEntity<String> sendResetMail(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email not registered!");
        }

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);

        String resetLink = "https://8080-cbaacefbecfdaacedbdfdaffabfbdede.premiumproject.examly.io/api/users/password/reset-form?token=" + token;

        try {
            sendHtmlMail(email, resetLink);
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("Error sending email: " + e.getMessage());
        }

        return ResponseEntity.ok("Reset mail sent to " + email);
    }

    private void sendHtmlMail(String email, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Insurance Claim – Password Reset Request");

        String htmlContent =
                "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "  <meta http-equiv='X-UA-Compatible' content='IE=edge' />" +
                "  <title>Insurance Claim | Password Reset</title>" +
                "  <style type='text/css'>" +
                "    :root{" +
                "      --brand:#0b5cff;" +
                "      --brand-600:#0a4fe0;" +
                "      --brand-700:#0947c8;" +
                "      --brand-800:#083da9;" +
                "      --accent:#22c55e;" +
                "      --warning:#f59e0b;" +
                "      --danger:#ef4444;" +
                "      --muted:#6b7280;" +
                "      --text:#0f172a;" +
                "      --subtext:#334155;" +
                "      --bg:#0b1220;" +
                "      --bg-soft:#0e1628;" +
                "      --card:#0f172a;" +
                "      --card-raise:#111b31;" +
                "      --border:#1f2a44;" +
                "      --ring:#60a5fa;" +
                "      --shadow:0 20px 60px rgba(2,6,23,.45);" +
                "      --grad1:linear-gradient(135deg,rgba(11,92,255,.95),rgba(34,197,94,.95));" +
                "      --grad2:linear-gradient(120deg,#0b5cff,#22c55e);" +
                "      --glass:rgba(255,255,255,.06);" +
                "    }" +
                "    html,body{margin:0;padding:0;background:var(--bg);color:#e2e8f0;font-family:Inter,Segoe UI,Roboto,Helvetica,Arial,sans-serif;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}" +
                "    .outer{width:100%;padding:28px 14px;background:radial-gradient(1200px 600px at 10% -10%,#123,#0000),radial-gradient(1000px 500px at 90% 0%,#02102a,#0000),radial-gradient(800px 400px at 50% 100%,#0a1a38,#0000)}" +
                "    .container{width:100%;max-width:820px;margin:0 auto;background:var(--card);border:1px solid var(--border);border-radius:18px;overflow:hidden;box-shadow:var(--shadow)}" +
                "    .ribbon{position:relative;overflow:hidden}" +
                "    .ribbon:before{content:\"\";position:absolute;inset:0;background:radial-gradient(1200px 1200px at -10% -10%,rgba(11,92,255,.35),transparent 45%),radial-gradient(900px 900px at 110% -10%,rgba(34,197,94,.35),transparent 45%);pointer-events:none}" +
                "    .header{background:var(--grad1);padding:32px 32px 28px 32px;color:#fff}" +
                "    .brand-row{display:flex;align-items:center;gap:14px}" +
                "    .logo{width:46px;height:46px;border-radius:14px;background:conic-gradient(from 0deg at 50% 50%,#fff0 0 270deg,#fff 0 360deg),linear-gradient(135deg,#ffffff1a,#ffffff33);box-shadow:inset 0 0 0 1px rgba(255,255,255,.25);backdrop-filter:blur(6px)}" +
                "    .brand-title{font-size:18px;font-weight:800;letter-spacing:.3px;text-shadow:0 2px 10px rgba(0,0,0,.25)}" +
                "    .badge{margin-left:auto;padding:6px 10px;border-radius:999px;background:#ffffff2b;font-size:12px;font-weight:700}" +
                "    .hero{padding:18px 0 4px}" +
                "    .hero h1{margin:0;font-size:28px;line-height:1.2;font-weight:900;letter-spacing:.2px}" +
                "    .sub{margin-top:6px;color:#e5e7ebcc}" +
                "    .body{background:linear-gradient(180deg,rgba(255,255,255,.03),transparent),var(--card);padding:28px}" +
                "    .panel{display:flex;gap:18px;flex-wrap:wrap}" +
                "    .left{flex:1 1 320px;min-width:280px}" +
                "    .right{flex:1 1 260px;min-width:240px}" +
                "    .callout{border:1px solid var(--border);background:linear-gradient(180deg,rgba(11,92,255,.08),rgba(11,92,255,.03));padding:16px 16px;border-radius:14px;color:#e2e8f0}" +
                "    .callout .t{font-weight:800;letter-spacing:.2px}" +
                "    .list{margin:14px 0 0 18px;padding:0}" +
                "    .list li{margin:8px 0;color:#cbd5e1}" +
                "    .btn-wrap{text-align:center;margin:26px 0 6px}" +
                "    .btn{display:inline-block;text-decoration:none;background:var(--brand);color:#fff!important;padding:14px 22px;border-radius:12px;font-weight:800;font-size:15px;letter-spacing:.3px;border:1px solid rgba(255,255,255,.18);box-shadow:0 8px 30px rgba(11,92,255,.35),inset 0 -6px 10px rgba(0,0,0,.2)}" +
                "    .btn:hover{background:var(--brand-600)}" +
                "    .btn:active{transform:translateY(1px)}" +
                "    .alt{margin-top:10px;text-align:center;font-size:12px;color:#a3b0c2}" +
                "    .alt .code{display:inline-block;padding:4px 8px;border-radius:8px;background:#0b1220;border:1px dashed #28406a;color:#cde0ff;font-family:ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, \"Liberation Mono\", \"Courier New\", monospace}" +
                "    .grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:14px;margin-top:26px}" +
                "    .card{border:1px solid var(--border);border-radius:14px;background:linear-gradient(180deg,rgba(255,255,255,.02),rgba(255,255,255,.01));padding:16px 16px}" +
                "    .card .h{margin:6px 0 8px;font-size:14px;font-weight:900;color:#e5e7eb}" +
                "    .tips{padding-left:18px;margin:0}" +
                "    .tips li{margin:6px 0;color:#cbd5e1}" +
                "    .kpis{display:flex;gap:10px;margin-top:16px;flex-wrap:wrap}" +
                "    .pill{padding:8px 12px;border-radius:999px;background:#0b1220;border:1px solid #243b68;color:#cfe1ff;font-size:11px;font-weight:800;letter-spacing:.2px}" +
                "    .footer{background:linear-gradient(180deg,#0b1220,#0b1220);padding:18px 28px;font-size:12px;color:#94a3b8;text-align:center;border-top:1px solid var(--border)}" +
                "    .divider{height:1px;background:linear-gradient(90deg,transparent,#1f2a44,transparent);margin:24px 0}" +
                "    .note{font-size:13px;color:#8aa0bf}" +
                "    .cta-bar{margin:16px 0 0;display:flex;align-items:center;gap:10px;justify-content:center;flex-wrap:wrap}" +
                "    .mini{display:inline-flex;align-items:center;gap:8px;padding:8px 12px;border-radius:10px;border:1px solid #27406e;background:rgba(255,255,255,.03);font-size:12px;color:#d1e2ff}" +
                "    .shield{width:16px;height:16px;display:inline-block;border-radius:4px;background:conic-gradient(from 180deg,#22c55e,#0b5cff,#22c55e)}" +
                "    .warn{color:#fbbf24}" +
               
                "    .danger{color:#fca5a5}" +
                "    .success{color:#86efac}" +
                "    .watermark{position:absolute;inset:auto -60px -60px auto;opacity:.18;filter:blur(3px);transform:rotate(-12deg);font-weight:900;font-size:120px;pointer-events:none}" +
                "    .sp{height:10px}" +
                "    @media (max-width:560px){" +
                "      .body{padding:22px}" +
                "      .header{padding:24px}" +
                "      .hero h1{font-size:24px}" +
                "    }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='outer'>" +
                "    <div class='container ribbon'>" +
                "      <div class='header'>" +
                "        <div class='brand-row'>" +
                "          <span class='logo'></span>" +
                "          <span class='brand-title'>Insurance Claim Portal</span>" +
                "          <span class='badge'>Security Notice</span>" +
                "        </div>" +
                "        <div class='hero'>" +
                "          <h1>Reset your password securely</h1>" +
                "          <div class='sub'>We detected a request to change your account password</div>" +
                "        </div>" +
                "      </div>" +
                "      <div class='body'>" +
                "        <div class='panel'>" +
                "          <div class='left'>" +
                "            <div class='callout'>" +
                "              <div class='t'>Important</div>" +
                "              <div class='sp'></div>" +
                "              Create a password that is hard to guess and unique to this account. Recommended length is at least 8 characters with a combination of upper and lower case letters, numbers, and special symbols." +
                "              <ul class='list'>" +
                "                <li>Avoid reusing passwords from other apps.</li>" +
                "                <li>Enable two-factor authentication if available.</li>" +
                "                <li>Never share your password with anyone.</li>" +
                "              </ul>" +
                "            </div>" +
                "            <div class='btn-wrap'>" +
                "              <a href='" + resetLink + "' class='btn' target='_blank' rel='noopener'>Reset My Password</a>" +
                "            </div>" +
                "            <div class='alt'>If the button doesn’t work, copy and paste this link into your browser:<br/><span class='code'>" + resetLink + "</span></div>" +
                "            <div class='cta-bar'>" +
                "              <span class='mini'><span class='shield'></span> SSL Secured</span>" +
                "              <span class='mini'>Device Verified</span>" +
                "              <span class='mini'>Session Protected</span>" +
                "            </div>" +
                "          </div>" +
                "          <div class='right'>" +
                "            <div class='grid'>" +
                "              <div class='card'>" +
                "                <div class='h'>Keep your account safe</div>" +
                "                <ul class='tips'>" +
                "                  <li>Beware of phishing links and fake emails.</li>" +
                "                  <li>Use a password manager to store credentials.</li>" +
                "                  <li>Update your password periodically.</li>" +
                "                </ul>" +
                "              </div>" +
                "              <div class='card'>" +
                "                <div class='h'>Need help?</div>" +
                "                <ul class='tips'>" +
                "                  <li>Contact support: <a href='mailto:support@insuranceclaim.com' style='color:#cfe1ff;text-decoration:none;border-bottom:1px dotted #cfe1ff;'>support@insuranceclaim.com</a></li>" +
                "                  <li>Visit Help Center for FAQs</li>" +
                "                  <li>Report suspicious activity immediately</li>" +
                "                </ul>" +
                "              </div>" +
                "            </div>" +
                "          </div>" +
                "        </div>" +
                "        <div class='divider'></div>" +
                "        <div class='note'>If you didn’t request this change, you can safely ignore this email—your password will remain unchanged.</div>" +
                "      </div>" +
                "      <div class='footer'>© " + Calendar.getInstance().get(Calendar.YEAR) + " Insurance Claim. All rights reserved.</div>" +
                "      <div class='watermark'>INSURE</div>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    @GetMapping("/reset-form")
    public String showResetForm(@RequestParam("token") String token) {
        if (!resetTokens.containsKey(token)) {
            return "<!DOCTYPE html>" +
                   "<html lang='en'><head><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1.0'><title>Invalid Token</title>" +
                   "<style>" +
                   "  :root{--bg:#0b1220;--card:#0f172a;--border:#1f2a44;--text:#e2e8f0;--muted:#94a3b8;--brand:#ef4444;--ring:#fca5a5;--grad:radial-gradient(600px 600px at 10% -10%,#1b2a4a,transparent 50%),radial-gradient(700px 700px at 90% -10%,#2a1530,transparent 50%)}" +
                   "  html,body{height:100%}body{margin:0;background:var(--bg);font-family:Inter,Segoe UI,Roboto,Helvetica,Arial,sans-serif;color:var(--text)}" +
                   "  .wrap{min-height:100%;display:flex;align-items:center;justify-content:center;padding:24px;background:var(--grad)}" +
                   "  .card{background:linear-gradient(180deg,rgba(255,255,255,.03),rgba(255,255,255,.01)),var(--card);border:1px solid var(--border);border-radius:18px;box-shadow:0 20px 60px rgba(2,6,23,.45);max-width:560px;width:100%;overflow:hidden}" +
                   "  .head{background:linear-gradient(135deg,#ef4444,#b91c1c);color:#fff;padding:22px 24px}" +
                   "  .head h2{margin:0;font-size:22px;font-weight:900;letter-spacing:.2px}" +
                   "  .body{padding:22px 24px}" +
                   "  .msg{color:var(--muted);line-height:1.7}" +
                   "  .actions{margin-top:16px}" +
                   "  .btn{display:inline-block;padding:12px 18px;border-radius:12px;background:#28a745;color:green;text-decoration:none;font-weight:800;border:1px solid #475569}" +
                   "  .btn:hover{background:#218838}" +
                   "  .icon{width:42px;height:42px;border-radius:12px;background:linear-gradient(135deg,#fecaca,#fee2e2);display:inline-flex;align-items:center;justify-content:center;color:#b91c1c;font-weight:900;margin-right:10px}" +
                   "  .row{display:flex;align-items:center;gap:10px}" +
                   "  .foot{background:#0b1220;padding:16px 22px;color:#94a3b8;text-align:center;border-top:1px solid var(--border)}" +
                   "</style></head>" +
                   "<body><div class='wrap'><div class='card'>" +
                   "<div class='head'><div class='row'><div class='icon'>!</div><h2>Invalid or expired link</h2></div></div>" +
                   "<div class='body'><p class='msg'>The password reset link you used is invalid or has expired. Please request a new password reset email to continue.</p><div class='actions'><a class='btn' href='/'>Back to Home</a></div></div>" +
                   "<div class='foot'>Need help? Contact support@insuranceclaim.com</div>" +
                   "</div></div></body></html>";
        }

        return "<!DOCTYPE html>" +
               "<html lang='en'>" +
               "<head>" +
               "  <meta charset='UTF-8' />" +
               "  <meta name='viewport' content='width=device-width, initial-scale=1.0' />" +
               "  <title>Reset Password • Insurance Claim</title>" +
               "  <style>" +
               "    :root{--bg:#0b1220;--panel:#0f172a;--card:#0f172a;--card2:#111b31;--border:#1f2a44;--ring:#60a5fa;--brand:#0b5cff;--brand-700:#0947c8;--text:#e2e8f0;--muted:#94a3b8;--good:#22c55e;--bad:#ef4444;--input:#0b1220;--shadow:0 20px 60px rgba(2,6,23,.45)}" +
               "    html,body{height:100%}body{margin:0;background:radial-gradient(900px 600px at 10% -10%,#10203d,transparent 60%),radial-gradient(900px 600px at 90% -10%,#12203f,transparent 60%),var(--bg);font-family:Inter,Segoe UI,Roboto,Helvetica,Arial,sans-serif;color:var(--text)}" +
               "    .wrap{min-height:100%;display:flex;align-items:center;justify-content:center;padding:24px}" +
               "    .card{width:100%;max-width:560px;background:linear-gradient(180deg,rgba(255,255,255,.03),rgba(255,255,255,.01)),var(--card);border:1px solid var(--border);border-radius:18px;box-shadow:var(--shadow);overflow:hidden}" +
               "    .head{background:linear-gradient(135deg,#0b5cff,#22c55e);color:#fff;padding:22px 24px;border-bottom:1px solid rgba(255,255,255,.2)}" +
               "    .head h1{margin:0;font-size:22px;font-weight:900;letter-spacing:.2px}" +
               "    .subtitle{margin:6px 0 0;color:#e2e8f0cc;font-size:13px}" +
               "    .body{padding:22px 24px}" +
               "    .field{margin-bottom:16px}" +
               "    label{display:block;font-size:12px;color:#cbd5e1;margin-bottom:6px;font-weight:700;letter-spacing:.2px}" +
               "    .input{width:100%;padding:13px 14px;border-radius:12px;border:1px solid var(--border);background:linear-gradient(180deg,#0b1220,#0b1220);color:#e2e8f0;font-size:14px;outline:none;transition:box-shadow .2s,border-color .2s}" +
               "    .input::placeholder{color:#70819b}" +
               "    .input:focus{border-color:#1d4ed8;box-shadow:0 0 0 4px rgba(59,130,246,.25)}" +
               "    .meter{height:8px;border-radius:8px;background:#0b1220;border:1px solid #1b2b4d;overflow:hidden;margin-top:8px}" +
               "    .meter .bar{height:100%;width:0;background:linear-gradient(90deg,#ef4444,#f59e0b,#22c55e);transition:width .25s}" +
               "    .help{font-size:12px;color:#94a3b8;margin-top:6px}" +
               "    .error{color:var(--bad);font-size:12px;margin-top:6px;display:none}" +
               "    .success{color:var(--good);font-size:12px;margin-top:6px;display:none}" +
               "    .actions{margin-top:18px;display:flex;flex-direction:column;gap:10px}" +
               "    .btn{appearance:none;border:none;background:linear-gradient(135deg,#0b5cff,#2563eb);color:#fff;font-weight:900;padding:13px;border-radius:12px;cursor:pointer;font-size:15px;letter-spacing:.2px;border:1px solid rgba(255,255,255,.18);box-shadow:0 10px 30px rgba(11,92,255,.35),inset 0 -6px 10px rgba(0,0,0,.2)}" +
               "    .btn:hover{background:linear-gradient(135deg,#0a4fe0,#1d4ed8)}" +
               "    .strength{display:flex;align-items:center;gap:8px;margin-top:8px;font-size:12px;color:#cbd5e1}" +
               "    .dot{width:8px;height:8px;border-radius:50%;background:#64748b}" +
               "    .dot.ok{background:#22c55e}" +
               "    .dot.mid{background:#f59e0b}" +
               "    .dot.bad{background:#ef4444}" +
               "    .row{display:flex;gap:10px;align-items:center}" +
               "    .eye{user-select:none;display:inline-flex;align-items:center;justify-content:center;width:38px;height:38px;border-radius:10px;border:1px solid var(--border);background:#0b1220;color:#cbd5e1;font-weight:900;cursor:pointer}" +
               "    .footer{padding:16px 22px;background:#0b1220;border-top:1px solid var(--border);font-size:12px;color:#94a3b8;text-align:center}" +
               "    .brandline{display:flex;align-items:center;gap:8px;justify-content:center;margin-top:10px}" +
               "    .shield{width:14px;height:14px;border-radius:4px;background:conic-gradient(from 180deg,#22c55e,#0b5cff,#22c55e)}" +
               "    @media (max-width:560px){.body{padding:18px}.head{padding:18px}.head h1{font-size:20px}}" +
               "  </style>" +
               "</head>" +
               "<body>" +
               "  <div class='wrap'>" +
               "    <div class='card'>" +
               "      <div class='head'>" +
               "        <h1>Set a New Password</h1>" +
               "        <div class='subtitle'>Choose a strong password to secure your Insurance Claim account</div>" +
               "      </div>" +
               "      <div class='body'>" +
               "        <form id='resetForm' method='POST' action='/api/users/password/update'>" +
               "          <input type='hidden' name='token' value='" + token + "'/>" +
               "          <div class='field'>" +
               "            <label for='newPassword'>New Password</label>" +
               "            <div class='row'>" +
               "              <input id='newPassword' name='newPassword' class='input' type='password' minlength='8' required placeholder='At least 8 characters' />" +
               "              <span id='toggle1' class='eye'>•</span>" +
               "            </div>" +
               "            <div class='meter'><div id='meterBar' class='bar'></div></div>" +
               "            <div class='strength'><span id='dot1' class='dot'></span><span id='dot2' class='dot'></span><span id='dot3' class='dot'></span><span id='strengthText'>Strength: Unknown</span></div>" +
               "            <div class='help'>Use a unique password. Mix letters, numbers, and symbols.</div>" +
               "            <div id='lenError' class='error'>Password must be at least 8 characters.</div>" +
               "          </div>" +
               "          <div class='field'>" +
               "            <label for='confirmPassword'>Confirm Password</label>" +
               "            <div class='row'>" +
               "              <input id='confirmPassword' class='input' type='password' minlength='8' required placeholder='Re-enter new password' />" +
               "              <span id='toggle2' class='eye'>•</span>" +
               "            </div>" +
               "            <div id='matchError' class='error'>Passwords do not match.</div>" +
               "            <div id='matchSuccess' class='success'>Passwords match.</div>" +
               "          </div>" +
               "          <div class='actions'>" +
               "            <button type='submit' id='submitBtn' class='btn'>Update Password</button>" +
               "          </div>" +
               "        </form>" +
               "        <div class='brandline'><span class='shield'></span> End-to-end encrypted • SSL Secured</div>" +
               "      </div>" +
               "      <div class='footer'>Having trouble? Contact <a href='mailto:support@insuranceclaim.com' style='color:#cfe1ff;text-decoration:none;border-bottom:1px dotted #cfe1ff;'>support@insuranceclaim.com</a></div>" +
               "    </div>" +
               "  </div>" +
               "  <script>" +
               "    (function(){" +
               "      var form=document.getElementById('resetForm');" +
               "      var newPassword=document.getElementById('newPassword');" +
               "      var confirmPassword=document.getElementById('confirmPassword');" +
               "      var lenError=document.getElementById('lenError');" +
               "      var matchError=document.getElementById('matchError');" +
               "      var matchSuccess=document.getElementById('matchSuccess');" +
               "      var submitBtn=document.getElementById('submitBtn');" +
               "      var meterBar=document.getElementById('meterBar');" +
               "      var dot1=document.getElementById('dot1');" +
               "      var dot2=document.getElementById('dot2');" +
               "      var dot3=document.getElementById('dot3');" +
               "      var strengthText=document.getElementById('strengthText');" +
               "      var t1=document.getElementById('toggle1');" +
               "      var t2=document.getElementById('toggle2');" +
               "      function score(p){" +
               "        var s=0;" +
               "        if(p.length>=8)s+=1;" +
               "        if(/[A-Z]/.test(p))s+=1;" +
               "        if(/[0-9]/.test(p))s+=1;" +
               "        if(/[^A-Za-z0-9]/.test(p))s+=1;" +
               "        return Math.min(3,s);" +
               "      }" +
               "      function updateStrength(){" +
               "        var p=newPassword.value;var sc=score(p);" +
               "        var w=['10%','55%','100%'][sc-1]||'5%';" +
               "        meterBar.style.width=p.length?w:'0%';" +
               "        dot1.className='dot'+(sc>0?' bad':'');" +
               "        dot2.className='dot'+(sc>1?' mid':'');" +
               "        dot3.className='dot'+(sc>2?' ok':'');" +
               "        strengthText.textContent=p.length?(sc<2?'Strength: Weak':(sc===2?'Strength: Medium':'Strength: Strong')):'Strength: Unknown';" +
               "      }" +
               "      function validateLengths(){var ok=newPassword.value.length>=8;lenError.style.display=ok?'none':'block';return ok;}" +
               "      function validateMatch(){var ok=newPassword.value===confirmPassword.value&&confirmPassword.value.length>0;matchError.style.display=ok?'none':'block';matchSuccess.style.display=ok?'block':'none';return ok;}" +
               "      newPassword.addEventListener('input',function(){validateLengths();validateMatch();updateStrength();});" +
               "      confirmPassword.addEventListener('input',function(){validateMatch();});" +
               "      function toggle(el,inp){el.addEventListener('click',function(){inp.type=inp.type==='password'?'text':'password';});}" +
               "      toggle(t1,newPassword);toggle(t2,confirmPassword);" +
               "      form.addEventListener('submit',function(e){if(!validateLengths()||!validateMatch()){e.preventDefault();return false;}});" +
               "      updateStrength();" +
               "    })();" +
               "  </script>" +
               "</body>" +
               "</html>";
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {

        String email = resetTokens.get(token);
        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired reset token!");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }

        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepository.save(user);

        resetTokens.remove(token);

        return ResponseEntity.ok("Password updated successfully for " + email);
    }
}
