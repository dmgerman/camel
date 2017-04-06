begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2016 Red Hat, Inc.  *  * Red Hat licenses this file to you under the Apache License, version  * 2.0 (the "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or  * implied.  See the License for the specific language governing  * permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_comment
comment|/**  * A global {@code Supplier} of {@code SSLContextParameters} to be used in Camel registry.  */
end_comment

begin_interface
DECL|interface|GlobalSSLContextParametersSupplier
specifier|public
interface|interface
name|GlobalSSLContextParametersSupplier
extends|extends
name|Supplier
argument_list|<
name|SSLContextParameters
argument_list|>
block|{ }
end_interface

end_unit

