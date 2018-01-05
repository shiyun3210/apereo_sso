<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html lang="en">
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="session" />
  <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <title>CAS &#8211; Central Authentication Service</title>

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="${ctx }/css/cas.css" />
    <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />

</head>
<body id="cas">

<div id="container">
  <header>
    <a id="logo" href="http://www.apereo.org" title="<spring:message code="logo.title" />">Apereo</a>
    <h1>Central Authentication Service (CAS)</h1>
  </header>
  <div id="content">
