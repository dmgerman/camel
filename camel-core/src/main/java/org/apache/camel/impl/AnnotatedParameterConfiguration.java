begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|AccessibleObject
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link ParameterConfiguration} which comes from a field or setter method  * which has access to its underlying annotations to be able to expose additional validation  * and conversion metadata for the parameter via annotations  */
end_comment

begin_class
DECL|class|AnnotatedParameterConfiguration
specifier|public
class|class
name|AnnotatedParameterConfiguration
extends|extends
name|ParameterConfiguration
block|{
DECL|field|accessibleObject
specifier|private
specifier|final
name|AccessibleObject
name|accessibleObject
decl_stmt|;
DECL|method|AnnotatedParameterConfiguration (String name, Class<?> type, AccessibleObject accessibleObject)
specifier|public
name|AnnotatedParameterConfiguration
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|AccessibleObject
name|accessibleObject
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|accessibleObject
operator|=
name|accessibleObject
expr_stmt|;
block|}
DECL|method|getAccessibleObject ()
specifier|public
name|AccessibleObject
name|getAccessibleObject
parameter_list|()
block|{
return|return
name|accessibleObject
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"AnnotatedParameterConfiguration["
operator|+
name|getName
argument_list|()
operator|+
literal|" on "
operator|+
name|accessibleObject
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

