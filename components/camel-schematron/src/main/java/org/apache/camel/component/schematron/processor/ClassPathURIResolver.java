begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|schematron
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_comment
comment|/**  * Class path resolver for schematron templates  */
end_comment

begin_class
DECL|class|ClassPathURIResolver
specifier|public
class|class
name|ClassPathURIResolver
implements|implements
name|URIResolver
block|{
DECL|field|rulesDir
specifier|private
specifier|final
name|String
name|rulesDir
decl_stmt|;
comment|/**      * Constructor setter for rules directory path.      */
DECL|method|ClassPathURIResolver (final String rulesDir)
specifier|public
name|ClassPathURIResolver
parameter_list|(
specifier|final
name|String
name|rulesDir
parameter_list|)
block|{
name|this
operator|.
name|rulesDir
operator|=
name|rulesDir
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolve (String href, String base)
specifier|public
name|Source
name|resolve
parameter_list|(
name|String
name|href
parameter_list|,
name|String
name|base
parameter_list|)
throws|throws
name|TransformerException
block|{
return|return
operator|new
name|StreamSource
argument_list|(
name|ClassPathURIResolver
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|rulesDir
operator|.
name|concat
argument_list|(
name|File
operator|.
name|separator
argument_list|)
operator|.
name|concat
argument_list|(
name|href
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

