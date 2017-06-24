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
name|Injector
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

begin_comment
comment|/**  * Default implementation of a type converter registry used for  *<a href="http://camel.apache.org/type-converter.html">type converters</a> in Camel.  *<p/>  * This implementation will load type converters up-front on startup.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultTypeConverter
specifier|public
class|class
name|DefaultTypeConverter
extends|extends
name|BaseTypeConverterRegistry
block|{
DECL|field|loadTypeConverters
specifier|private
specifier|final
name|boolean
name|loadTypeConverters
decl_stmt|;
DECL|method|DefaultTypeConverter (PackageScanClassResolver resolver, Injector injector, FactoryFinder factoryFinder, boolean loadTypeConverters)
specifier|public
name|DefaultTypeConverter
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|FactoryFinder
name|factoryFinder
parameter_list|,
name|boolean
name|loadTypeConverters
parameter_list|)
block|{
name|super
argument_list|(
name|resolver
argument_list|,
name|injector
argument_list|,
name|factoryFinder
argument_list|)
expr_stmt|;
name|this
operator|.
name|loadTypeConverters
operator|=
name|loadTypeConverters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|allowNull ()
specifier|public
name|boolean
name|allowNull
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isRunAllowed ()
specifier|public
name|boolean
name|isRunAllowed
parameter_list|()
block|{
comment|// as type converter is used during initialization then allow it to always run
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// core type converters is always loaded which does not use any classpath scanning
comment|// and therefore is fast
name|loadCoreTypeConverters
argument_list|()
expr_stmt|;
if|if
condition|(
name|loadTypeConverters
condition|)
block|{
name|int
name|core
init|=
name|typeMappings
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// load type converters up front
name|loadTypeConverters
argument_list|()
expr_stmt|;
name|int
name|additional
init|=
name|typeMappings
operator|.
name|size
argument_list|()
operator|-
name|core
decl_stmt|;
comment|// report how many type converters we have loaded
name|log
operator|.
name|info
argument_list|(
literal|"Type converters loaded (core: {}, classpath: {})"
argument_list|,
name|core
argument_list|,
name|additional
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

