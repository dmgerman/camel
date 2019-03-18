begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ClassResolver
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
name|FactoryFinder
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
name|FactoryFinderResolver
import|;
end_import

begin_comment
comment|/**  * Default factory finder.  */
end_comment

begin_class
DECL|class|DefaultFactoryFinderResolver
specifier|public
class|class
name|DefaultFactoryFinderResolver
implements|implements
name|FactoryFinderResolver
block|{
DECL|method|resolveDefaultFactoryFinder (ClassResolver classResolver)
specifier|public
name|FactoryFinder
name|resolveDefaultFactoryFinder
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|)
block|{
return|return
name|resolveFactoryFinder
argument_list|(
name|classResolver
argument_list|,
literal|"META-INF/services/org/apache/camel/"
argument_list|)
return|;
block|}
DECL|method|resolveFactoryFinder (ClassResolver classResolver, String resourcePath)
specifier|public
name|FactoryFinder
name|resolveFactoryFinder
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|resourcePath
parameter_list|)
block|{
return|return
operator|new
name|DefaultFactoryFinder
argument_list|(
name|classResolver
argument_list|,
name|resourcePath
argument_list|)
return|;
block|}
block|}
end_class

end_unit

