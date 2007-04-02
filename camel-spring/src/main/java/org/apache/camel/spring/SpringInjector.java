begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|impl
operator|.
name|converter
operator|.
name|Injector
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
name|converter
operator|.
name|ReflectionInjector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * A Spring implementation of {@link Injector} allowing Spring to be used to inject newly constructed type converters  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringInjector
specifier|public
class|class
name|SpringInjector
extends|extends
name|ReflectionInjector
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SpringInjector
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|applicationContext
specifier|private
specifier|final
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|SpringInjector (ApplicationContext applicationContext)
specifier|public
name|SpringInjector
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
DECL|method|newInstance (Class type)
specifier|public
name|Object
name|newInstance
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|String
index|[]
name|names
init|=
name|applicationContext
operator|.
name|getBeanNamesForType
argument_list|(
name|type
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|names
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|// lets instantiate the single bean
return|return
name|applicationContext
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|names
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Too many beans of type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" available: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
operator|+
literal|" so ignoring Spring configuration"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// lets instantiate the bean
name|Object
name|answer
init|=
name|super
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
comment|// TODO now lets inject spring...
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

