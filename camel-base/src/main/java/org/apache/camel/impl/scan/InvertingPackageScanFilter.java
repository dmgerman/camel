begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.scan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|scan
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
name|PackageScanFilter
import|;
end_import

begin_comment
comment|/**  * Package scan filter for inverting the match result of a subfilter. If the  * subfilter would match and return<tt>true</tt> this filter will invert that  * match and return<tt>false</tt>.  */
end_comment

begin_class
DECL|class|InvertingPackageScanFilter
specifier|public
class|class
name|InvertingPackageScanFilter
implements|implements
name|PackageScanFilter
block|{
DECL|field|filter
specifier|private
name|PackageScanFilter
name|filter
decl_stmt|;
DECL|method|InvertingPackageScanFilter (PackageScanFilter filter)
specifier|public
name|InvertingPackageScanFilter
parameter_list|(
name|PackageScanFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
DECL|method|matches (Class<?> type)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|!
name|filter
operator|.
name|matches
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"!["
operator|+
name|filter
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

