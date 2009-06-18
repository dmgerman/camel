begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Converter
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
name|AnnotationTypeConverterLoader
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
name|PackageScanClassResolver
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
name|TypeConverterRegistry
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

begin_class
DECL|class|OsgiAnnotationTypeConverterLoader
specifier|public
class|class
name|OsgiAnnotationTypeConverterLoader
extends|extends
name|AnnotationTypeConverterLoader
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|OsgiAnnotationTypeConverterLoader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|OsgiAnnotationTypeConverterLoader (PackageScanClassResolver packageScanClassResolver)
specifier|public
name|OsgiAnnotationTypeConverterLoader
parameter_list|(
name|PackageScanClassResolver
name|packageScanClassResolver
parameter_list|)
block|{
name|super
argument_list|(
name|packageScanClassResolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|load (TypeConverterRegistry registry)
specifier|public
name|void
name|load
parameter_list|(
name|TypeConverterRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Activator
operator|.
name|TypeConverterEntry
name|entry
range|:
name|Activator
operator|.
name|getTypeConverterEntries
argument_list|()
control|)
block|{
name|OsgiPackageScanClassResolver
name|resolver
init|=
operator|new
name|OsgiPackageScanClassResolver
argument_list|(
name|entry
operator|.
name|bundle
operator|.
name|getBundleContext
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|packages
init|=
name|entry
operator|.
name|converterPackages
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|entry
operator|.
name|converterPackages
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
init|=
name|resolver
operator|.
name|findAnnotated
argument_list|(
name|Converter
operator|.
name|class
argument_list|,
name|packages
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
name|type
range|:
name|classes
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading converter class: "
operator|+
name|ObjectHelper
operator|.
name|name
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|loadConverterMethods
argument_list|(
name|registry
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

