<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Kiev.ua</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </head>

    <body  style="background-color:gold;"> <!-- колір фону-->
        <div class="container" style="background-color:gold;">
            <!-- style="font-family:'Comic Sans MS';" додав шрифт для напису -->
           <h3 style="font-family:'Comic Sans MS';"> <img height="50" width="55" src="<c:url value="/static/1.png"/>"/><a href="/">Contacts List</a></h3>

            <nav class="navbar navbar-default">
                <div class="container-fluid" style="background-color:gold;">
                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="background-color:gold;">
                        <ul id="groupList" class="nav navbar-nav">
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="add_contact" class="btn btn-primary">Add Contact</button></li>
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="add_group" class="btn btn-primary">Add Group</button></li>
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="delete_contact" class="btn btn-primary">Delete Contact</button></li>
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="delete_group" class="btn btn-primary">Delete group</button></li>
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="transfer_contact" class="btn btn-primary">Transfer</button></li>
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="reset" class="btn btn-primary">Reset</button></li>
                            <li style="font-family:'Comic Sans MS';"><button type="button" id="download" class="btn btn-primary">Download Contacts</button></li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Groups <span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="/">Default</a></li>
                                    <c:forEach items="${groups}" var="group">
                                        <li><a href="/group/${group.id}">${group.name}</a></li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </ul>
                        <form class="navbar-form navbar-left" role="search" action="/search" method="post">
                            <div class="form-group">
                                <input type="text" class="form-control" name="pattern" placeholder="Search">
                            </div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </form>
                    </div><!-- /.navbar-collapse -->
                </div><!-- /.container-fluid -->
            </nav>

            <table class="table table-striped">
                <thead>
                <tr>
                    <td></td>
                    <td><b>Name</b></td>
                    <td><b>Surname</b></td>
                    <td><b>Phone</b></td>
                    <td><b>E-mail</b></td>
                    <td><b>Group</b></td>
                </tr>
                </thead>
                <c:forEach items="${contacts}" var="contact">
                    <tr>
                        <td><input type="checkbox" name="toDelete[]" value="${contact.id}" id="checkbox_${contact.id}"/></td>
                        <td>${contact.name}</td>
                        <td>${contact.surname}</td>
                        <td>${contact.phone}</td>
                        <td>${contact.email}</td>
                        <c:choose>
                            <c:when test="${contact.group ne null}">
                                <td>${contact.group.name}</td>
                            </c:when>
                            <c:otherwise>
                                <td>Default</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </table>

            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${allPages ne null}">
                        <c:forEach var="i" begin="1" end="${allPages}">
                            <li><a href="/?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${byGroupPages ne null}">
                        <c:forEach var="i" begin="1" end="${byGroupPages}">
                            <li><a href="/group/${groupId}?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                        </c:forEach>
                    </c:if>
                </ul>
            </nav>
        </div>

        <script>
            $('.dropdown-toggle').dropdown();

            $('#add_contact').click(function(){
                window.location.href='/contact_add_page';
            });

            $('#add_group').click(function(){
                window.location.href='/group_add_page';
            });

            $('#transfer_contact').click(function(){
                window.location.href='/contact_transfer_page';
            });

            $('#reset').click(function(){
                window.location.href='/reset';
            });
            $('#delete_group').click(function(){
                window.location.href='/group/delete';
            });
            $('#download').click(function(){
                window.location.href='/download';
            });

            $('#delete_contact').click(function(){
                let data = { 'toDelete[]' : []};
                $(":checked").each(function() {
                    data['toDelete[]'].push($(this).val());
                });
                $.post("/contact/delete", data, function(data, status) {
                    window.location.reload();
                });
            });
        </script>
    </body>
</html>