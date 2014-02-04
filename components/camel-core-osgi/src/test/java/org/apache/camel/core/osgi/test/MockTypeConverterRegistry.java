begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|TypeConverter
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
name|TypeConverterRegistry
import|;
end_import

begin_class
DECL|class|MockTypeConverterRegistry
specifier|public
class|class
name|MockTypeConverterRegistry
implements|implements
name|TypeConverterRegistry
block|{
DECL|field|typeConverters
specifier|private
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|typeConverters
init|=
operator|new
name|ArrayList
argument_list|<
name|TypeConverter
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|fallbackTypeConverters
specifier|private
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|fallbackTypeConverters
init|=
operator|new
name|ArrayList
argument_list|<
name|TypeConverter
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getTypeConverters ()
specifier|public
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|getTypeConverters
parameter_list|()
block|{
return|return
name|typeConverters
return|;
block|}
DECL|method|getFallbackTypeConverters ()
specifier|public
name|List
argument_list|<
name|TypeConverter
argument_list|>
name|getFallbackTypeConverters
parameter_list|()
block|{
return|return
name|fallbackTypeConverters
return|;
block|}
DECL|method|addTypeConverter (Class<?> toType, Class<?> fromType, TypeConverter typeConverter)
specifier|public
name|void
name|addTypeConverter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|,
name|TypeConverter
name|typeConverter
parameter_list|)
block|{
name|typeConverters
operator|.
name|add
argument_list|(
name|typeConverter
argument_list|)
expr_stmt|;
block|}
DECL|method|removeTypeConverter (Class<?> toType, Class<?> fromType)
specifier|public
name|boolean
name|removeTypeConverter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|)
block|{
comment|// noop
return|return
literal|true
return|;
block|}
DECL|method|addFallbackTypeConverter (TypeConverter typeConverter, boolean canPromote)
specifier|public
name|void
name|addFallbackTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|,
name|boolean
name|canPromote
parameter_list|)
block|{
name|fallbackTypeConverters
operator|.
name|add
argument_list|(
name|typeConverter
argument_list|)
expr_stmt|;
block|}
DECL|method|lookup (Class<?> toType, Class<?> fromType)
specifier|public
name|TypeConverter
name|lookup
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|toType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|fromType
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
DECL|method|setInjector (Injector injector)
specifier|public
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getInjector ()
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|getStatistics ()
specifier|public
name|Statistics
name|getStatistics
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|typeConverters
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

