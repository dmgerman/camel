begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.axis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|axis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|rpc
operator|.
name|ServiceException
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
name|ReportIncidentService_PortType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|remoting
operator|.
name|jaxrpc
operator|.
name|ServletEndpointSupport
import|;
end_import

begin_comment
comment|/**  * Axis webservice  */
end_comment

begin_class
DECL|class|AxisReportIncidentService
specifier|public
class|class
name|AxisReportIncidentService
extends|extends
name|ServletEndpointSupport
implements|implements
name|ReportIncidentService_PortType
block|{
DECL|field|service
specifier|private
name|ReportIncidentService
name|service
decl_stmt|;
annotation|@
name|Override
DECL|method|onInit ()
specifier|protected
name|void
name|onInit
parameter_list|()
throws|throws
name|ServiceException
block|{
comment|// get hold of the spring bean from the application context
name|service
operator|=
operator|(
name|ReportIncidentService
operator|)
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
literal|"incidentservice"
argument_list|)
expr_stmt|;
block|}
DECL|method|reportIncident (InputReportIncident parameters)
specifier|public
name|OutputReportIncident
name|reportIncident
parameter_list|(
name|InputReportIncident
name|parameters
parameter_list|)
throws|throws
name|RemoteException
block|{
comment|// delegate to the real service
return|return
name|service
operator|.
name|reportIncident
argument_list|(
name|parameters
argument_list|)
return|;
block|}
block|}
end_class

end_unit

