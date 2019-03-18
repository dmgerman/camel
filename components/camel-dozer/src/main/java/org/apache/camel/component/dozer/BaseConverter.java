begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dozermapper
operator|.
name|core
operator|.
name|ConfigurableCustomConverter
import|;
end_import

begin_comment
comment|/**  * Configurable converters in Dozer are not thread-safe if a single converter   * instance is used.  One thread could step on the parameter being used by   * another thread since setParameter() is called first and convert() is called   * separately.  This implementation holds a copy of the parameter in   * thread-local storage which eliminates the possibility of collision between  * threads on a single converter instance.  *   * Any converter which is referenced by ID with the Dozer component should  * extend this class.  It is recommended to call done() in a finally block   * in the implementation of convert() to clean up the value stored in the   * thread local.  */
end_comment

begin_class
DECL|class|BaseConverter
specifier|public
specifier|abstract
class|class
name|BaseConverter
implements|implements
name|ConfigurableCustomConverter
block|{
DECL|field|localParameter
specifier|private
name|ThreadLocal
argument_list|<
name|String
argument_list|>
name|localParameter
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|setParameter (String parameter)
specifier|public
name|void
name|setParameter
parameter_list|(
name|String
name|parameter
parameter_list|)
block|{
name|localParameter
operator|.
name|set
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
DECL|method|done ()
specifier|public
name|void
name|done
parameter_list|()
block|{
name|localParameter
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|getParameter ()
specifier|public
name|String
name|getParameter
parameter_list|()
block|{
return|return
name|localParameter
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

