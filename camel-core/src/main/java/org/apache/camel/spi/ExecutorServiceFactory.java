begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_interface
DECL|interface|ExecutorServiceFactory
specifier|public
interface|interface
name|ExecutorServiceFactory
block|{
DECL|method|newExecutorService (ThreadPoolProfile profile)
name|ExecutorService
name|newExecutorService
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

