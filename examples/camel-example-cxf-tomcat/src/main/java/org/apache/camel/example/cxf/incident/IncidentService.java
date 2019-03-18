begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.incident
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
name|incident
package|;
end_package

begin_comment
comment|/**  * Interface with the services we want to expose as web services using code first.  *<p/>  * This is a basic example, you can use the JAX-WS annotations to control the contract.  */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_interface
DECL|interface|IncidentService
specifier|public
interface|interface
name|IncidentService
block|{
comment|/**      * Operation to report an incident      */
DECL|method|reportIncident (InputReportIncident input)
name|OutputReportIncident
name|reportIncident
parameter_list|(
name|InputReportIncident
name|input
parameter_list|)
function_decl|;
comment|/**      * Operation to get the status of an incident      */
DECL|method|statusIncident (InputStatusIncident input)
name|OutputStatusIncident
name|statusIncident
parameter_list|(
name|InputStatusIncident
name|input
parameter_list|)
function_decl|;
block|}
end_interface

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

