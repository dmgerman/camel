begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
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
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|CamelContext
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
name|Produce
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
name|impl
operator|.
name|CamelPostProcessorHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|support
operator|.
name|AnnotationMemberProvider
import|;
end_import

begin_comment
comment|/**  * Injects values into the {@link Produce} injection point  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ProduceInjector
specifier|public
class|class
name|ProduceInjector
extends|extends
name|CamelPostProcessorHelper
implements|implements
name|AnnotationMemberProvider
argument_list|<
name|Produce
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|ProduceInjector (CamelContext camelContext)
specifier|public
name|ProduceInjector
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|isNullParameterAllowed (Produce produce, Method method, Class<?> aClass, int index)
specifier|public
name|boolean
name|isNullParameterAllowed
parameter_list|(
name|Produce
name|produce
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|provide (Produce inject, TypeLiteral<?> typeLiteral, Field field)
specifier|public
name|Object
name|provide
parameter_list|(
name|Produce
name|inject
parameter_list|,
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|typeLiteral
parameter_list|,
name|Field
name|field
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|field
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|injectionPointName
init|=
name|field
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|endpointRef
init|=
name|inject
operator|.
name|ref
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|inject
operator|.
name|uri
argument_list|()
decl_stmt|;
return|return
name|getInjectionValue
argument_list|(
name|type
argument_list|,
name|uri
argument_list|,
name|endpointRef
argument_list|,
name|injectionPointName
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|provide (Produce inject, TypeLiteral<?> typeLiteral, Method method, Class<?> aClass, int index)
specifier|public
name|Object
name|provide
parameter_list|(
name|Produce
name|inject
parameter_list|,
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|typeLiteral
parameter_list|,
name|Method
name|method
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|parameterTypes
index|[
name|index
index|]
decl_stmt|;
name|String
name|injectionPointName
init|=
name|ObjectHelper
operator|.
name|getPropertyName
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|String
name|endpointRef
init|=
name|inject
operator|.
name|ref
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|inject
operator|.
name|uri
argument_list|()
decl_stmt|;
return|return
name|getInjectionValue
argument_list|(
name|type
argument_list|,
name|uri
argument_list|,
name|endpointRef
argument_list|,
name|injectionPointName
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

