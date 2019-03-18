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
name|exporter
operator|.
name|ZipExporter
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
name|AbstractStreamExporterImpl
import|;
end_import

begin_comment
comment|/**  * An implementation of the zip exporter that does not compress entries,  * for compatibility with spring-boot nested jar structure.  */
end_comment

begin_class
DECL|class|SpringBootZipExporterImpl
specifier|public
class|class
name|SpringBootZipExporterImpl
extends|extends
name|AbstractStreamExporterImpl
implements|implements
name|ZipExporter
block|{
DECL|method|SpringBootZipExporterImpl (Archive<?> archive)
specifier|public
name|SpringBootZipExporterImpl
parameter_list|(
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
block|}
annotation|@
name|Override
DECL|method|exportAsInputStream ()
specifier|public
name|InputStream
name|exportAsInputStream
parameter_list|()
block|{
comment|// Create export delegate
specifier|final
name|AbstractExporterDelegate
argument_list|<
name|InputStream
argument_list|>
name|exportDelegate
init|=
operator|new
name|SpringBootZipExporterDelegate
argument_list|(
name|this
operator|.
name|getArchive
argument_list|()
argument_list|)
decl_stmt|;
comment|// Export and get result
return|return
name|exportDelegate
operator|.
name|export
argument_list|()
return|;
block|}
block|}
end_class

end_unit

