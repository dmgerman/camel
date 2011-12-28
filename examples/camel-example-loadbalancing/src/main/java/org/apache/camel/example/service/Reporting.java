begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|service
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
name|Body
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
name|Header
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
name|model
operator|.
name|Report
import|;
end_import

begin_class
DECL|class|Reporting
specifier|public
class|class
name|Reporting
block|{
DECL|method|updateReport (@ody Report report, @Header(R) String name)
specifier|public
name|Report
name|updateReport
parameter_list|(
annotation|@
name|Body
name|Report
name|report
parameter_list|,
annotation|@
name|Header
argument_list|(
literal|"minaServer"
argument_list|)
name|String
name|name
parameter_list|)
block|{
name|report
operator|.
name|setReply
argument_list|(
literal|"Report updated from MINA server running on: "
operator|+
name|name
argument_list|)
expr_stmt|;
comment|// send the report updated
return|return
name|report
return|;
block|}
block|}
end_class

end_unit

