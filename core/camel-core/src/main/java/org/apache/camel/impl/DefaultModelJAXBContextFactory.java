begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|model
operator|.
name|Constants
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
name|spi
operator|.
name|ModelJAXBContextFactory
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link org.apache.camel.spi.ModelJAXBContextFactory}.  */
end_comment

begin_class
DECL|class|DefaultModelJAXBContextFactory
specifier|public
class|class
name|DefaultModelJAXBContextFactory
implements|implements
name|ModelJAXBContextFactory
block|{
DECL|field|context
specifier|private
specifier|volatile
name|JAXBContext
name|context
decl_stmt|;
DECL|method|newJAXBContext ()
specifier|public
name|JAXBContext
name|newJAXBContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|getPackages
argument_list|()
argument_list|,
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|context
return|;
block|}
DECL|method|getPackages ()
specifier|protected
name|String
name|getPackages
parameter_list|()
block|{
return|return
name|Constants
operator|.
name|JAXB_CONTEXT_PACKAGES
return|;
block|}
DECL|method|getClassLoader ()
specifier|protected
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
return|;
block|}
block|}
end_class

end_unit
