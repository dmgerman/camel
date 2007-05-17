begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_comment
comment|/**  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MessageHeaderAccessException
specifier|public
class|class
name|MessageHeaderAccessException
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
literal|3996286386119163309L
decl_stmt|;
DECL|field|propertyName
specifier|private
name|String
name|propertyName
decl_stmt|;
DECL|method|MessageHeaderAccessException (String propertyName, MessagingException e)
specifier|public
name|MessageHeaderAccessException
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|MessagingException
name|e
parameter_list|)
block|{
name|super
argument_list|(
literal|"Error accessing header: "
operator|+
name|propertyName
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
block|}
DECL|method|getPropertyName ()
specifier|public
name|String
name|getPropertyName
parameter_list|()
block|{
return|return
name|propertyName
return|;
block|}
block|}
end_class

end_unit

