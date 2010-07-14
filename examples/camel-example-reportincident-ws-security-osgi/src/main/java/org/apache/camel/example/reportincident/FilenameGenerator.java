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

begin_comment
comment|/**  * Plain java class to be used for filename generation based on the reported incident  */
end_comment

begin_class
DECL|class|FilenameGenerator
specifier|public
class|class
name|FilenameGenerator
block|{
DECL|method|generateFilename (InputReportIncident input)
specifier|public
name|String
name|generateFilename
parameter_list|(
name|InputReportIncident
name|input
parameter_list|)
block|{
comment|// compute the filename
return|return
literal|"incident-"
operator|+
name|input
operator|.
name|getIncidentId
argument_list|()
operator|+
literal|".txt"
return|;
block|}
block|}
end_class

end_unit

