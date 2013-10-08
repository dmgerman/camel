begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_expr_stmt
operator|.
name|incident
expr_stmt|;
end_expr_stmt

begin_comment
comment|/**  * Output report message  */
end_comment

begin_class
DECL|class|OutputReportIncident
specifier|public
class|class
name|OutputReportIncident
block|{
DECL|field|code
specifier|private
name|String
name|code
decl_stmt|;
DECL|method|getCode ()
specifier|public
name|String
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
DECL|method|setCode (String code)
specifier|public
name|void
name|setCode
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
block|}
block|}
end_class

end_unit

