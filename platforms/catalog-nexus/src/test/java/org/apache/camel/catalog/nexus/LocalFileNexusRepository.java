begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.nexus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|nexus
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
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
DECL|class|LocalFileNexusRepository
specifier|public
class|class
name|LocalFileNexusRepository
extends|extends
name|ComponentNexusRepository
block|{
annotation|@
name|Override
DECL|method|createNexusUrl ()
specifier|protected
name|URL
name|createNexusUrl
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/nexus-sample-result.xml"
argument_list|)
decl_stmt|;
return|return
operator|new
name|URL
argument_list|(
literal|"file:"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

