begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_comment
comment|/**  * @deprecated we should not use runtime exception for the attachment binding  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MessageHeaderNamesAccessException
specifier|public
class|class
name|MessageHeaderNamesAccessException
extends|extends
name|RuntimeMailException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6744171518099741324L
decl_stmt|;
DECL|method|MessageHeaderNamesAccessException (Throwable e)
specifier|public
name|MessageHeaderNamesAccessException
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to access the Mail message property names"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

