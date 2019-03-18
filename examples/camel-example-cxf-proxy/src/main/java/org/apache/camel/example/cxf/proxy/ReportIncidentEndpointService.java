begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.proxy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|proxy
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
name|example
operator|.
name|reportincident
operator|.
name|InputReportIncident
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
name|example
operator|.
name|reportincident
operator|.
name|OutputReportIncident
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
name|example
operator|.
name|reportincident
operator|.
name|ReportIncidentEndpoint
import|;
end_import

begin_comment
comment|/**  * This is the implementation of the real web service  */
end_comment

begin_class
DECL|class|ReportIncidentEndpointService
specifier|public
class|class
name|ReportIncidentEndpointService
implements|implements
name|ReportIncidentEndpoint
block|{
DECL|method|reportIncident (InputReportIncident in)
specifier|public
name|OutputReportIncident
name|reportIncident
parameter_list|(
name|InputReportIncident
name|in
parameter_list|)
block|{
comment|// just log and return a fixed response
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\n\nInvoked real web service: id="
operator|+
name|in
operator|.
name|getIncidentId
argument_list|()
operator|+
literal|" by "
operator|+
name|in
operator|.
name|getGivenName
argument_list|()
operator|+
literal|" "
operator|+
name|in
operator|.
name|getFamilyName
argument_list|()
operator|+
literal|"\n\n\n"
argument_list|)
expr_stmt|;
name|OutputReportIncident
name|out
init|=
operator|new
name|OutputReportIncident
argument_list|()
decl_stmt|;
name|out
operator|.
name|setCode
argument_list|(
literal|"OK;"
operator|+
name|in
operator|.
name|getIncidentId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
block|}
end_class

end_unit

