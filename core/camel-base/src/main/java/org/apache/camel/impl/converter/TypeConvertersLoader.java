begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TypeConverters
import|;
end_import

begin_comment
comment|/**  * A type converter loader, that<b>only</b> supports scanning a {@link org.apache.camel.TypeConverters} class  * for methods that has been annotated with {@link org.apache.camel.Converter}.  */
end_comment

begin_class
DECL|class|TypeConvertersLoader
specifier|public
class|class
name|TypeConvertersLoader
extends|extends
name|AnnotationTypeConverterLoader
block|{
DECL|field|typeConverters
specifier|private
specifier|final
name|TypeConverters
name|typeConverters
decl_stmt|;
comment|/**      * Creates the loader      *      * @param typeConverters  The implementation that has the type converters      */
DECL|method|TypeConvertersLoader (TypeConverters typeConverters)
specifier|public
name|TypeConvertersLoader
parameter_list|(
name|TypeConverters
name|typeConverters
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|TypeConvertersPackageScanClassResolver
argument_list|(
name|typeConverters
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|typeConverters
operator|=
name|typeConverters
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
name|String
name|name
init|=
name|typeConverters
operator|.
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
operator|new
name|String
index|[]
block|{
name|name
block|}
return|;
block|}
block|}
end_class

end_unit

