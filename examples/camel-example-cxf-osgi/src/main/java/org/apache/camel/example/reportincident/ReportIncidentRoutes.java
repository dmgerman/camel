begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.reportincident
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|reportincident
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * Our routes that we can build using Camel DSL as we extend the RouteBuilder class.  *<p/>  * In the configure method we have all kind of DSL methods we use for expressing our routes.  */
end_comment

begin_class
DECL|class|ReportIncidentRoutes
specifier|public
class|class
name|ReportIncidentRoutes
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// webservice response for OK
name|OutputReportIncident
name|ok
init|=
operator|new
name|OutputReportIncident
argument_list|()
decl_stmt|;
name|ok
operator|.
name|setCode
argument_list|(
literal|"0"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"cxf:bean:reportIncident"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|InputReportIncident
operator|.
name|class
argument_list|)
comment|// TODO remove?
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|constant
argument_list|(
literal|"request-${date:now:yyyy-MM-dd-HHmmssSSS}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/inbox/"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
name|ok
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

