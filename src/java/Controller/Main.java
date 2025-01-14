/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Beans.DoctorTime;
import Beans.EChanneling;
import Beans.Specialization;
import Beans.Time;
import Beans.User;
import Model.EChannelings;
import Model.Patients;
import Model.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Toshiba
 */
@WebServlet(name = "Main", urlPatterns = {"/Main"})
public class Main extends HttpServlet {
    
    HttpSession session;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession();
        String action = request.getParameter("action");
        String page = null;
        User user = null;
        request.setAttribute("user", user);
        if (action == null || session.equals(null)) {
            request.getRequestDispatcher("Index.jsp").forward(request, response);
            return;
        }else if(action.equals("patientsetting")){
                request.getRequestDispatcher("PatientSetting.jsp").forward(request,response);
        }else if(action.equals("patientsprofile")){
                request.getRequestDispatcher("PatientProfile.jsp").forward(request,response);
        }else{
            if(action.equals("logout")){
                session.invalidate();
                response.sendRedirect("Index.jsp");
            }else if (action.equals("echannel")) {
                EChannelings ec = new EChannelings();
                List<Specialization> s = new ArrayList();
                List<Time> t = new ArrayList();
                s = ec.getSpecialization();
                t = ec.getTimes();
                request.setAttribute("specializations", s);
                request.setAttribute("times", t);
                page = "SearchDoctor.jsp";
            } else if (action.equals("viewdoctor")) {
                page = "Doctor.jsp";
                System.out.println("view doctor");
                User u1 = new User();
                u1 = (User) session.getAttribute("user");
                String doctorId = request.getParameter("doctorid");
                User u = new User();
                List<DoctorTime> dt = new ArrayList();
                u.setDoctorId(doctorId);
                EChannelings ec = new EChannelings();
                u = ec.viewDoctor(u);
                dt = ec.doctorSessions(u);
                System.out.println(doctorId);
                request.setAttribute("doctor", u);
                request.setAttribute("user", u1);
                request.setAttribute("sessions", dt);
            } else if (action.equals("appointment")) {
                String doctorId = request.getParameter("doctorid");
                String date = request.getParameter("date");
                page = "EChannel.jsp";
                
                User u = new User();
                DoctorTime doctorTime = new DoctorTime();
                List<DoctorTime> dts = new ArrayList();
                u.setDoctorId(doctorId);
                EChannelings ec = new EChannelings();
                u = ec.viewDoctor(u);
                dts = ec.doctorSessions(u);
                for (DoctorTime dt : dts) {
                    if (dt.getDate().equals(date)) {
                        doctorTime.setDoctorId(doctorId);
                        doctorTime.setDate(date);
                        doctorTime.setDayId(dt.getDayId());
                        doctorTime.setDay(dt.getDay());
                        doctorTime.setNextSession(dt.getNextSession());
                        int i = Integer.parseInt(dt.getAppointments()) + 1;
                        doctorTime.setAppointments(String.valueOf(String.valueOf(i)));
                        
                        request.setAttribute("channeldetails", doctorTime);
                        request.setAttribute("doctor", u);
                    }
                }
            }
            request.getRequestDispatcher(page).forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession();
        String action = request.getParameter("action");
        String page = null;
        PrintWriter out = response.getWriter();
        User user = new User();
        if (action != null) {
            if (action.equals("login")) {
                System.out.println("login");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                System.out.println(username+"  uname");
                System.out.println(password+"  pass");
                Users users = new Users();
                user = users.checkLogin(username, password);
                if (!"1".equals(user.getFound())) {
                    System.out.println("Invalid username or password");
                    request.setAttribute("error", "Invalid username or password");
                    request.getRequestDispatcher("Index.jsp").forward(request, response);
                    response.sendRedirect("Index.jsp");
                    return;
                }

                session.setAttribute("user", user);
                request.setAttribute("user", user);               
                if (user.getTypeId().equals("4") || user.getTypeId().equals("5") ) {
                    request.getRequestDispatcher("Index.jsp").forward(request, response);
                    response.sendRedirect("Index.jsp");
                    return;
                }else{
                    user.setError("user type not found");
                    System.out.println("user type not found");
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("Index.jsp").forward(request, response);
                    response.sendRedirect("Index.jsp");
                    return;
                }               
            }else if(action.equals("checkusername")){
                Users users = new Users();
                String username = request.getParameter("username");
                int available = users.checkUsername(username);
                String y="";
                if (available == 1){
                    y = "<font style='font-size:80%;' color = '#008080'>Available</font>";
                }else{
                    y = "<font style='font-size:80%;' color = '#FF2F00'>Already Exist</font>";
                }
                response.getWriter().write(y);
            }else if(action.equals("signup")){
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                System.out.println(username);
                System.out.println(password);
                System.out.println(email);
                Users users = new Users();
                User user1 = new User();
                user1.setUsername(username);
                user1.setPassword(password);
                user1.setEmail(email);
                user1 = users.signup(user1);
                response.getWriter().write(user1.getSuccess());
            }else if (action.equals("searchdoctors")) {
                page = "SearchDoctor.jsp";
                EChannelings ec = new EChannelings();
                List<Specialization> s = new ArrayList();
                List<Time> t = new ArrayList();
                s = ec.getSpecialization();
                t = ec.getTimes();
                request.setAttribute("specializations", s);
                request.setAttribute("times", t);

                String[] searchInputs = new String[4];
                searchInputs[0] = request.getParameter("doctor");
                searchInputs[1] = request.getParameter("specialty");
                searchInputs[2] = request.getParameter("daydate");
                searchInputs[3] = request.getParameter("time");
                if (searchInputs[3].isEmpty()) {
                    searchInputs[3] = null;
                }
                System.out.println(searchInputs[0]);
                System.out.println(searchInputs[1]);
                System.out.println(searchInputs[2]);
                System.out.println(searchInputs[3]);

                List<User> doctors = new ArrayList();
                doctors = ec.searchDoctor(searchInputs);
                if (doctors.isEmpty()) {
                    request.setAttribute("norecords", "No Records Found");
                }
                request.setAttribute("results", "Search Results");
                request.setAttribute("doctors", doctors);
                request.setAttribute("doctor", searchInputs[0]);
                request.setAttribute("specialty", searchInputs[1]);
                request.setAttribute("daydate", searchInputs[2]);
                request.setAttribute("time", searchInputs[3]);

                System.out.println("echannel");
            } else if (action.equals("elogin")) {
                System.out.println("login");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String doctorId = request.getParameter("doctorid");
                String date = request.getParameter("date");

                Users users = new Users();
                user = users.checkLogin(username, password);
                if (!"1".equals(user.getFound()) || !user.getTypeId().equals("4") && !user.getTypeId().equals("3")) {
                    page = "Doctor.jsp";
                } else {
                    page = "EChannel.jsp";
                    User u = new User();
                    DoctorTime doctorTime = new DoctorTime();
                    List<DoctorTime> dts = new ArrayList();
                    u.setDoctorId(doctorId);
                    EChannelings ec = new EChannelings();
                    u = ec.viewDoctor(u);
                    dts = ec.doctorSessions(u);
                    for (DoctorTime dt : dts) {
                        if (dt.getDate().equals(date)) {
                            doctorTime.setDoctorId(doctorId);
                            doctorTime.setDate(date);
                            doctorTime.setDayId(dt.getDayId());
                            doctorTime.setDay(dt.getDay());
                            doctorTime.setNextSession(dt.getNextSession());
                            int i = Integer.parseInt(dt.getAppointments()) + 1;
                            doctorTime.setAppointments(String.valueOf(String.valueOf(i)));
                        }
                    }

                    session.setAttribute("user", user);
                    request.setAttribute("channeldetails", doctorTime);
                    request.setAttribute("doctor", u);
                    request.setAttribute("user", user);
                }
            }else if (action.equals("channel")) {
                page = "EChannel.jsp";
                String doctorid,date,time,fName,lName,nic,contact,email,notes;
                doctorid = request.getParameter("doctorid");
                date = request.getParameter("date");
                System.out.println(date);
                time = request.getParameter("time");
                fName = request.getParameter("fname");
                lName = request.getParameter("lname");
                nic = request.getParameter("nic");
                contact = request.getParameter("contact");
                email = request.getParameter("email");
                notes = request.getParameter("notes");
                EChannelings ec = new EChannelings();
                EChanneling channeling = new EChanneling();
                channeling.setDoctorId(doctorid);
                channeling.setDate(date);
                channeling.setTime(time);
                channeling.setPatientFName(fName);
                channeling.setPatientLName(lName);
                channeling.setNic(nic);
                channeling.setNotes(notes);
                channeling = ec.makeAppointment(channeling);
                
                response.getWriter().write(channeling.getSuccess());
                return;
                
            }else if (action.equals("updatePatients")) {
                String email = request.getParameter("patientemail");
                String curPassword = request.getParameter("curpassword");
                String newPassword = request.getParameter("newpassword");
                String conformpassword = request.getParameter("conformpassword");

                Patients patient = new Patients();
                if (patient.emailValidation(email) == true) {

                    if (patient.passwordValidation(newPassword, conformpassword) == true) {
                        System.out.println(user.getUsername());
                        if (patient.EditPatients(email, newPassword, user.getUsername()) == true) {
                            request.setAttribute("updateSuccessMessage", "Succesfully Updated");
                            request.getRequestDispatcher("PatientSetting.jsp").forward(request, response);
                            return;
                        } else {
                            request.setAttribute("updateErrorMessage", "Can't Update Details. Try again");
                            request.getRequestDispatcher("PatientSetting.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("passworderrorMessage", "Password Does Not Match");
                        request.getRequestDispatcher("PatientSetting.jsp").forward(request, response);
                        return;
                    }

                } else {
                    request.setAttribute("emailerrorMessage", "Incorrect Email Address");
                    request.getRequestDispatcher("PatientSetting.jsp").forward(request, response);
                    return;
                }
            }
        }
        request.getRequestDispatcher(page).forward(request,response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
