begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Will load all type converters from camel-core without classpath scanning, which makes  * it much faster.  *<p/>  * The {@link CorePackageScanClassResolver} contains a hardcoded list of the type converter classes to load.  */
end_comment

begin_class
DECL|class|CoreTypeConverterLoader
specifier|public
class|class
name|CoreTypeConverterLoader
extends|extends
name|AnnotationTypeConverterLoader
block|{
DECL|method|CoreTypeConverterLoader ()
specifier|public
name|CoreTypeConverterLoader
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|CorePackageScanClassResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|findPackageNames ()
specifier|protected
name|String
index|[]
name|findPackageNames
parameter_list|()
throws|throws
name|IOException
block|{
comment|// this method doesn't change the behavior of the CorePackageScanClassResolver
return|return
operator|new
name|String
index|[]
block|{
literal|"org.apache.camel.converter"
block|,
literal|"org.apache.camel.component.bean"
block|,
literal|"org.apache.camel.component.file"
block|}
return|;
block|}
block|}
end_class

end_unit

