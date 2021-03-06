begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.springboot.arquillian
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|springboot
operator|.
name|arquillian
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ArchivePath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|impl
operator|.
name|base
operator|.
name|exporter
operator|.
name|AbstractExporterDelegate
import|;
end_import

begin_comment
comment|/**  * A spring-boot compatible zip exporter delegate.  * Uses {@link SpringBootZipOnDemandInputStream} not to compress entries for spring-boot  * nested jar structure compatibility.  */
end_comment

begin_class
DECL|class|SpringBootZipExporterDelegate
specifier|public
class|class
name|SpringBootZipExporterDelegate
extends|extends
name|AbstractExporterDelegate
argument_list|<
name|InputStream
argument_list|>
block|{
DECL|method|SpringBootZipExporterDelegate (final Archive<?> archive)
specifier|protected
name|SpringBootZipExporterDelegate
parameter_list|(
specifier|final
name|Archive
argument_list|<
name|?
argument_list|>
name|archive
parameter_list|)
block|{
name|super
argument_list|(
name|archive
argument_list|)
expr_stmt|;
if|if
condition|(
name|archive
operator|.
name|getContent
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot export a ZIP archive with no content: "
operator|+
name|archive
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|processNode (final ArchivePath path, final Node node)
specifier|protected
name|void
name|processNode
parameter_list|(
specifier|final
name|ArchivePath
name|path
parameter_list|,
specifier|final
name|Node
name|node
parameter_list|)
block|{
comment|// do nothing
block|}
annotation|@
name|Override
DECL|method|getResult ()
specifier|protected
name|InputStream
name|getResult
parameter_list|()
block|{
return|return
operator|new
name|SpringBootZipOnDemandInputStream
argument_list|(
name|getArchive
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

