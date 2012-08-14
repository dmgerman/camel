begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
package|;
end_package

begin_enum
DECL|enum|AckCode
specifier|public
enum|enum
name|AckCode
block|{
DECL|enumConstant|AA
DECL|enumConstant|CA
DECL|enumConstant|AR
DECL|enumConstant|CR
DECL|enumConstant|AE
DECL|enumConstant|CE
name|AA
argument_list|(
literal|false
argument_list|)
block|,
name|CA
argument_list|(
literal|false
argument_list|)
block|,
name|AR
argument_list|(
literal|true
argument_list|)
block|,
name|CR
argument_list|(
literal|true
argument_list|)
block|,
name|AE
argument_list|(
literal|true
argument_list|)
block|,
name|CE
argument_list|(
literal|true
argument_list|)
block|;
DECL|field|error
specifier|private
specifier|final
name|boolean
name|error
decl_stmt|;
DECL|method|AckCode (boolean error)
name|AckCode
parameter_list|(
name|boolean
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
DECL|method|isError ()
specifier|public
name|boolean
name|isError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
block|}
end_enum

end_unit

